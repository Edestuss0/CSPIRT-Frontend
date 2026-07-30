import {useQuery} from "@tanstack/react-query";
import {ComplaintsApi} from "../../../entities/complaints/api/complaints_api.ts";

export const useComplaints = (id: number)=> {
    return useQuery({
        queryKey: ["complaints", id],
        queryFn: () => ComplaintsApi.getComplaints(id),
    });
}