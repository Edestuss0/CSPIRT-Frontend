import {create} from "zustand";
import type {UserType} from "../../../../shared/entities/user/types/user_types.ts";
import {UserApi} from "../../../../shared/entities/user/api/user_api.ts";
import {type ScheduleAddFormValues, ScheduleAddLessonUsecase} from "../model/add_schedule_usecase.ts";

interface State {
    status: "idle" | "error" | "loading",
    error: string | null,
    teachers: UserType[] | null

    addSchedule: (form: ScheduleAddFormValues, type: "base" | "current") => Promise<boolean>
    getTeachers: () => Promise<void>
}

export const useChangeScheduleStore = create<State>()((set) => ({
    status: "idle",
    error: null,
    teachers: null,

    addSchedule: async (form: ScheduleAddFormValues, type: "base" | "current") => {
        set({status: "loading", error: null});

        try {
            const response = await ScheduleAddLessonUsecase(form, type);
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
    },

    getTeachers: async () => {
        set({status: "loading", teachers: null, error: null});

        try {
            const response = await UserApi.getStaff();
            set({error: null, status: "idle", teachers: response});
        } catch (e) {
            set({
                error: e instanceof Error ? e.message : "Неизвестная ошибка",
                status: "error",
            });
        }
    }
}))