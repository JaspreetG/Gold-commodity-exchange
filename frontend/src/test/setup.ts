import "@testing-library/jest-dom";

const mockFpLoad = vi.fn().mockResolvedValue({
  get: vi.fn().mockResolvedValue({ visitorId: "mock-fingerprint" }),
});

vi.mock("@fingerprintjs/fingerprintjs", () => ({
  default: { load: mockFpLoad },
  load: mockFpLoad,
}));
