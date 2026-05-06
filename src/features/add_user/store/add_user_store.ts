import {AddUserUseCase, type addUserValues} from "../models/add_user_usecase.ts";
import {create} from "zustand";

interface State {
    status: "idle" | "error" | "loading",
    error: string | null,
    
    addUser: (form: addUserValues) => Promise<boolean>
}

export const useAddUserStore = create<State>()((set) => ({
    status: "idle",
    error: null,
    
    addUser: async (form: addUserValues) => {
        set({status: "loading", error: null});
        
        try {
            const response = await AddUserUseCase(form);
            if (response) {
                set({status: "idle", error: null});
                return true;
            }
            return false;
        } catch (e) {
            set({
                error: e instanceof Error ? e.message : "Неизвестная ошибка",
                status: "error",
            });
            return false
        }
    }
}))