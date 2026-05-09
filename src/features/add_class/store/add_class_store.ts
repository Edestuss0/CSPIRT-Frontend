import {create} from "zustand";
import {type addClassFormValues, AddClassUsecase} from "../../class/models/add_class_usecase.ts";

interface State {
    status: "idle" | "error" | "loading",
    error: string | null,

    addClass: (form: addClassFormValues) => Promise<boolean>
}

export const useAddClassStore = create<State>()((set) => ({
    status: "idle",
    error: null,

    addClass: async (form: addClassFormValues): Promise<boolean> => {
        set({status: "loading", error: null});

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
    }
}))