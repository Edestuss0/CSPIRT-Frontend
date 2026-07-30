import { create } from "zustand";
import {UserApi} from "../../../entities/user/api/user_api.ts";
import type {AuthDto, meType} from "../../../entities/user/types/user_types.ts";

type AuthStatus =
    | "idle"
    | "loading"
    | "authenticated"
    | "unauthenticated";

interface AuthState {
    user: meType | null;
    error: string | null;
    status: AuthStatus;

    login: (dto: AuthDto) => Promise<boolean>;
    checkAuth: () => Promise<void>;
    clearError: () => void;
    refreshAuth: () => Promise<void>;
}

function getPublicErrorMessage(error: unknown): string {
    if (!(error instanceof Error)) return "Ошибка";

    const allowedMessages = new Set([
        "Некорректный логин или пароль",
        "Ошибка авторизации",
        "Сессия недействительна",
        "Некорректный формат профиля",
        "Некорректный ответ сервера авторизации",
    ]);

    return allowedMessages.has(error.message) ? error.message : "Ошибка";
}

export const useAuthStore = create<AuthState>()((set) => ({
    user: null,
    error: null,
    status: "idle",

    login: async (dto) => {
        set({
            status: "loading",
            error: null,
        });

        try {
            await UserApi.login(dto);
            
            
            const user = await UserApi.checkAuth();

            set({
                status: "authenticated",
                error: null,
                user: user,
            });
            
            return true;
        } catch (error) {
            set({
                user: null,
                status: "unauthenticated",
                error: getPublicErrorMessage(error),
            });

            return false;
        }
    },

    checkAuth: async () => {
        set({
            status: "loading",
            error: null,
        });

        try {
            const userData = await UserApi.checkAuth();

            set({
                status: "authenticated",
                error: null,
                user: userData,
            });
        } catch {
            set({
                user: null,
                status: "unauthenticated",
                error: "Сессия недействительна",
            });
        }
    },
    
    refreshAuth: async () => {
      set({status: "loading", error: null});
      
      try {
          await UserApi.refresh();
          
          const user = await UserApi.checkAuth();
          
          set({
             status: "authenticated",
             error: null,
             user: user, 
          });
          
      } catch {
          set({
              user: null,
              status: "unauthenticated",
              error: null,
          });
      }
    },
    

    clearError: () => {
        set({ error: null });
    },
}));