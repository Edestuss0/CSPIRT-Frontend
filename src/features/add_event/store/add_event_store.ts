import {create} from "zustand";
import {type addEventFormValues, addEventUsecase} from "../models/add_event_usecase.ts";

interface State {
    status: "idle" | "error" | "loading",
    error: string | null,

    addEvent: (form: addEventFormValues) => Promise<boolean>
}

export const useAddEventStore = create<State>()((set) => ({
    status: "idle",
    error: null,

    addEvent: async (form: addEventFormValues) => {
        set({status: "loading", error: null});

        try {
            const response = await addEventUsecase(form);
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