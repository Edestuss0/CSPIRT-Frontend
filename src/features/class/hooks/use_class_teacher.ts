import {useQuery} from "@tanstack/react-query";
import {classApi} from "../../../entities/class/api/class_api.ts";

export const useClassTeacher = (id: number) => {
    return useQuery({
       queryKey: ["teacher", id],
       queryFn: () => classApi.getClassTeacher(id), 
    });
}