import {useQuery} from "@tanstack/react-query";
import {classApi} from "../../../entities/class/api/class_api.ts";

export const useParallels = () => {
    return useQuery({
        queryFn: () => classApi.getParallels(),
        queryKey: ["parallels"]
    })
}