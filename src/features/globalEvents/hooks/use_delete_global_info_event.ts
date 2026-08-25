import { useMutation, useQueryClient } from "@tanstack/react-query";
import {deleteGlobalEventInfo} from "../models/global_events_usecases.ts";

export const useDeleteGlobalInfoEvent = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (id: number) => deleteGlobalEventInfo(id),

        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ["global_events"],
            });
        },
    });
};