import {useQuery} from "@tanstack/react-query";
import {ScheduleApi} from "../../../entities/schedule/api/schedule_api.ts";
import {ScheduleService} from "../../../entities/schedule/service/schedule_service.ts";

export const useTeacherSchedule = (id: number) => {
    return useQuery({
        queryKey: ["teacher_schedule", id],
        queryFn: async () => {
            const response = await ScheduleApi.getTeacherSchedule(id);
            return ScheduleService.sortSchedule(response);
        }
    });
}