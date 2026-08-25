import {useMutation, useQueryClient} from "@tanstack/react-query";
import {EventsApi} from "../../../entities/events/api/events_api.ts";
import type {AddEventFormType} from "../../../entities/events/types/events_types.ts";

export const useAddEvent = () => {
    const queryclient = useQueryClient();
    
    return useMutation({
        mutationFn: (form: AddEventFormType) => EventsApi.addEvent(form),
        onSuccess: async () => {
            await queryclient.invalidateQueries({queryKey: ["events"]});
        }
    })
}