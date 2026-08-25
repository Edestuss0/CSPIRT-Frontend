import { useMutation, useQueryClient } from "@tanstack/react-query";
import {addGlobalEventQuiz, type GlobalEventQuizAddForm} from "../models/global_events_usecases.ts";

export const useAddGlobalQuizEvent = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (form: GlobalEventQuizAddForm) =>
            addGlobalEventQuiz(form),

        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ["global_events"],
            });
        },
    });
};