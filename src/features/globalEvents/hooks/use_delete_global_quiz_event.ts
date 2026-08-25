import { useMutation, useQueryClient } from "@tanstack/react-query";
import {deleteGlobalEventQuiz} from "../models/global_events_usecases.ts";

export const useDeleteGlobalQuizEvent = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (id: number) => deleteGlobalEventQuiz(id),

        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ["global_events"],
            });
        },
    });
};