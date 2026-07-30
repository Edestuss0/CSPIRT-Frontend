import {useQuery} from "@tanstack/react-query";
import {classApi} from "../../../entities/class/api/class_api.ts";


export const useClassId = (id: number) => {
    return useQuery({
        queryKey: ["class_by_id", id],
        queryFn: () => classApi.getClassById(id),
    });
}