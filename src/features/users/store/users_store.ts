import {create} from "zustand";
import type {UserType} from "../../../shared/entities/user/types/user_types.ts";
import {classApi} from "../../../shared/entities/class/api/class_api.ts";

type status = "idle" | "loading" | "error";

interface State {
    status: status;
    error: string | null;
    message: string | null;
    users: UserType[] | null;
    
    getUsersByClass: (id: number) => Promise<void>;    
}

export const useUsersStore = create<State>()((set) => ({
    status: "idle",
    error: null,
    message: null,
    users: null,
    
    getUsersByClass: async (id: number)=> {
        set({status: "loading"});

        try {
            const response = await classApi.getUsersByClass(id);

            set({status: "idle", users: response, error: null});
        } catch (e) {
            set({
                error: e instanceof Error ? e.message : "Неизвестная ошибка",
                status: "error",
            });
        }
    },
}));