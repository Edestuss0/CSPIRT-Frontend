import { create } from "zustand";
import {classApi} from "../../../shared/entities/class/api/class_api.ts";
import type {ClassType} from "../../../shared/entities/class/types/class_types.ts";
import {UserApi} from "../../../shared/entities/user/api/user_api.ts";
import type {UserType} from "../../../shared/entities/user/types/user_types.ts";
import type {EventType} from "../../../shared/entities/events/types/events_types.ts";
import {EventsApi} from "../../../shared/entities/events/api/events_api.ts";

export type DashboardStatus = "loading" | "error" | "idle";

interface State {
    status: DashboardStatus;
    error: string | null;
    message: string | null;
    classes: ClassType[];
    staff: UserType[];
    events: EventType[];

    getClasses: () => Promise<void>;
    getStaff: () => Promise<void>;
    getEvents: () => Promise<void>;
}

export const useDashboardStore = create<State>()((set) => ({
    error: null,
    status: "idle",
    message: null,
    classes: [],
    staff: [],
    events: [],
    
    getClasses: async () => {
      set({status: "loading"});
      
      try {
          const response = await classApi.getClasses();
          
          set({status: "idle", classes: response, error: null});
      } catch (e) {
          set({
              error: e instanceof Error ? e.message : "Неизвестная ошибка",
              status: "error",
          });
      }
    },
    
    getStaff: async () => {
        set({status: "loading"});
        
        try {
            const response = await UserApi.getStaff();
            set({staff: response, error: null, status: "idle"});
        } catch (e) {
            set({
                error: e instanceof Error ? e.message : "Неизвестная ошибка",
                status: "error",
            });
        }
    },
    
    getEvents: async () => {
        set({status: "loading"});
        
        try {
            const response = await EventsApi.getEvents();
            set({status: "idle", events: response, error: null});
        } catch (e) {
            set({
                error: e instanceof Error ? e.message : "Неизвестная ошибка",
                status: "error",
            });
        }
    },
    
}));