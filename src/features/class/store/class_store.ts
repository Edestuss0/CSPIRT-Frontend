import {create} from "zustand";
import {classApi} from "../../../shared/entities/class/api/class_api.ts";
import type {ClassType} from "../../../shared/entities/class/types/class_types.ts";

type status = "idle" | "loading" | "error";

interface State {
    status: status;
    error: string | null;
    message: string | null;
    classes: ClassType[] | null;

    getClasses: () => Promise<void>;
}

export const useClassStore = create<State>()((set) => ({
    status: "idle",
    error: null,
    message: null,
    classes: null,

    getClasses: async () => {
        set({status: "loading"});

        try {
            const response = await classApi.getClasses();
            set({status: "idle", error: null, classes: response});
        } catch (e) {
            set({
                error: e instanceof Error ? e.message : "Неизвестная ошибка",
                status: "error",
            });
        }
    },
    
}));