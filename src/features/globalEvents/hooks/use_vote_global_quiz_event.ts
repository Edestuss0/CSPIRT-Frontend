import {useMutation, useQueryClient} from "@tanstack/react-query";
import {type GlobalEventVoteForm, voteGlobalEventQuiz} from "../models/global_events_usecases.ts";

export const useVoteGlobalQuizEvent = () => {
    const queryClient = useQueryClient();

    return useMutation({
        mutationFn: (form: GlobalEventVoteForm) =>
            voteGlobalEventQuiz(form),
        onSuccess: async () => {
            await queryClient.invalidateQueries({
                queryKey: ["global_events"],
            });
        },
    });
};