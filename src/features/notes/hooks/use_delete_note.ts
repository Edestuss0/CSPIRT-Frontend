import {useMutation, useQueryClient} from "@tanstack/react-query";
import {NotesApi} from "../../../shared/entities/notes/api/notes_api.ts";

export const useDeleteNote = () => {
    const queryclient = useQueryClient();

    return useMutation({
        mutationFn: ({id}: {id: number}) => NotesApi.deleteNote(id),
        onSuccess: async (data,variables) => {
            const {id} = variables;
            
            await queryclient.invalidateQueries({queryKey: ["complaints", id]});
        }
    })
}