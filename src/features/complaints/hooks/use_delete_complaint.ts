import {useMutation, useQueryClient} from "@tanstack/react-query";
import {ComplaintsApi} from "../../../shared/entities/complaints/api/complaints_api.ts";

export const useDeleteComplaint = () => {
    const queryclient = useQueryClient();

    return useMutation({
        mutationFn: ({id}: {id: number}) => ComplaintsApi.deleteComplaint(id),
        onSuccess: async (data,variables) => {
            const {id} = variables;
            
            await queryclient.invalidateQueries({queryKey: ["complaints", id]});
        }
    })
}