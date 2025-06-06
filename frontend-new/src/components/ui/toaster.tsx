import { useToast } from "@/hooks/use-toast";
import {
  Toast,
  ToastClose,
  ToastDescription,
  ToastProvider,
  ToastTitle,
  ToastViewport,
} from "@/components/ui/toast";

export function Toaster() {
  const { toasts } = useToast();

  return (
    <ToastProvider>
      {toasts.map(function ({ id, title, description, action, ...props }) {
        return (
          <Toast
            key={id}
            {...props}
            className="bg-white border border-gray-200 shadow-lg"
          >
            <div className="grid gap-1">
              {title && (
                <ToastTitle className="text-black font-normal">
                  {title}
                </ToastTitle>
              )}
              {description && (
                <ToastDescription className="text-gray-600 font-light">
                  {description}
                </ToastDescription>
              )}
            </div>
            {action}
            <ToastClose />
          </Toast>
        );
      })}
      <ToastViewport className="fixed top-4 right-4 z-[100] flex max-h-screen w-full flex-col-reverse p-4 sm:max-w-[420px]" />
    </ToastProvider>
  );
}
