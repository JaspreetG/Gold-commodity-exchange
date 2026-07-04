import { describe, it, expect, vi, beforeEach } from "vitest";

const mockApi = vi.hoisted(() => ({ post: vi.fn(), get: vi.fn() }));

vi.mock("../../lib/axios", () => ({
  axiosInstance: vi.fn(() => mockApi),
}));

import { useAuthStore } from "../useAuthStore";

const mockNavigate = vi.fn();

describe("useAuthStore", () => {
  beforeEach(() => {
    useAuthStore.setState({
      authUser: null,
      tempUser: null,
      isLoggingIn: false,
      isSigningUp: false,
      toasts: [],
      orders: [],
      pastTrades: [],
    });
    vi.clearAllMocks();
  });

  it("initial state is unauthenticated", () => {
    const state = useAuthStore.getState();
    expect(state.authUser).toBeNull();
    expect(state.tempUser).toBeNull();
    expect(state.isLoggingIn).toBe(false);
    expect(state.isSigningUp).toBe(false);
    expect(state.toasts).toEqual([]);
  });

  it("signIn adds a toast and throws error on failed login", async () => {
    mockApi.post.mockRejectedValueOnce({
      response: { data: { error: "Phone number not registered" } },
    });

    const state = useAuthStore.getState();
    await expect(
      state.login({ phone: "1234567890" }, mockNavigate as any)
    ).rejects.toThrow();

    const toasts = useAuthStore.getState().toasts;
    expect(toasts.length).toBeGreaterThan(0);
    expect(toasts[0].title).toBe("Login Failed");
    expect(toasts[0].description).toContain("Phone number not registered");
  });

  it("signIn redirects to register when account not found", async () => {
    mockApi.post.mockResolvedValueOnce({
      data: { redirect: "register" },
    });

    const state = useAuthStore.getState();
    await state.login({ phone: "1234567890" }, mockNavigate as any);

    expect(mockNavigate).toHaveBeenCalledWith("/signUp");
  });

  it("logout resets state", async () => {
    useAuthStore.setState({
      authUser: {
        userName: "testuser",
        phoneNumber: "1234567890",
        balances: { inr: 1000, gold: 5 },
      },
    });

    mockApi.post.mockResolvedValueOnce({});

    await useAuthStore.getState().logout();

    expect(useAuthStore.getState().authUser).toBeNull();
  });

  it("verifyTOTP updates state on success", async () => {
    mockApi.post.mockResolvedValueOnce({ data: {} });
    mockApi.get
      .mockResolvedValueOnce({
        data: { userName: "testuser", phoneNumber: "1234567890" },
      })
      .mockResolvedValueOnce({
        data: { balance: 5000, gold: 10 },
      });

    await useAuthStore.getState().verifyTOTP("1234567890", "123456", "fp123");

    const state = useAuthStore.getState();
    expect(state.authUser).not.toBeNull();
    expect(state.authUser?.userName).toBe("testuser");
    expect(state.authUser?.balances.inr).toBe(5000);
    expect(state.authUser?.balances.gold).toBe(10);
    expect(state.tempUser).toBeNull();
  });

  it("verifyTOTP throws on failure", async () => {
    mockApi.post.mockRejectedValueOnce({
      response: { data: { error: "Invalid TOTP" } },
    });

    await expect(
      useAuthStore.getState().verifyTOTP("1234567890", "000000", "fp123")
    ).rejects.toThrow();
  });

  it("addToast adds a toast to the list", () => {
    const id = useAuthStore.getState().addToast({
      title: "Test",
      description: "Test description",
    });
    expect(id).toBeDefined();
    const state = useAuthStore.getState();
    expect(state.toasts.length).toBe(1);
    expect(state.toasts[0].title).toBe("Test");
  });

  it("dismissToast marks a toast as closed", () => {
    const id = useAuthStore.getState().addToast({
      title: "Test",
      description: "Test description",
    });
    useAuthStore.getState().dismissToast(id);
    const toasts = useAuthStore.getState().toasts;
    const toast = toasts.find((t) => t.id === id);
    expect(toast?.open).toBe(false);
  });

  it("removeToast removes a toast from the list", () => {
    const id = useAuthStore.getState().addToast({
      title: "Test",
      description: "Test description",
    });
    useAuthStore.getState().removeToast(id);
    const toasts = useAuthStore.getState().toasts;
    expect(toasts.find((t) => t.id === id)).toBeUndefined();
  });
});
