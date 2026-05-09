import {create} from "zustand";
import {classApi} from "../../../shared/entities/class/api/class_api.ts";
import type {ClassType} from "../../../shared/entities/class/types/class_types.ts";
import {type addClassFormValues, AddClassUsecase} from "../models/add_class_usecase.ts";

type status = "idle" | "loading" | "error";

interface State {
    status: status;
    error: string | null;
    message: string | null;
    classes: ClassType[] | null;

    getClasses: () => Promise<void>;
    addClass: (form: addClassFormValues) => Promise<boolean>
}

export const useClassStore = create<State>()((set) => ({
    status: "idle",
    error: null,
    message: null,
    classes: null,

    getClasses: async () => {
        set({status: "loading", error: null, message: null});

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

    addClass: async (form: addClassFormValues): Promise<boolean> => {
        set({status: "loading", error: null, message: null});

        try {
            const response = await AddClassUsecase(form);
            if (response) {
                set({status: "idle", error: null});
                return true;
            } else {
                return false;
            }
        } catch (e) {
            set({
                error: e instanceof Error ? e.message : "Неизвестная ошибка",
                status: "error",
            });
            return false;
        }
    },
    
}));