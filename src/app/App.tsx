import { RouterProvider } from "react-router-dom";
import { router } from "./router.tsx";
import { AuthInitializer } from "../features/auth/ui/auth_initializer.tsx";
import { ErrorBoundary } from "../shared/lib/error/error_boundary.tsx";

export default function App() {
    return (
        <ErrorBoundary
            onError={(error, info) => {
                if (import.meta.env.DEV) {
                    console.error("App Error:", error, info);
                }
            }}
        >
            <AuthInitializer>
                <RouterProvider router={router} />
            </AuthInitializer>
        </ErrorBoundary>
    );
}