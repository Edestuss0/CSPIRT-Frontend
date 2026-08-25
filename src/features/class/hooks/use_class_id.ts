import {useQuery} from "@tanstack/react-query";
import {classApi} from "../../../entities/class/api/class_api.ts";


export function useClassId(id: number | string | undefined | null) {    return useQuery({
        queryKey: ["class_by_id", id],
        enabled: Boolean(id),
        queryFn: () => classApi.getClassById(id),
    });
}