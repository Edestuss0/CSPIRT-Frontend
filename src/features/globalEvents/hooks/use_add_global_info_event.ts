import {useMutation, useQueryClient} from "@tanstack/react-query";
import {addGlobalEventInfo, type GlobalEventInfoAddForm} from "../models/global_events_usecases.ts";

export const useAddGlobalInfoEvent = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (form: GlobalEventInfoAddForm) =>
            addGlobalEventInfo(form),
        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ["global_events"],
            });
        },
    });
};