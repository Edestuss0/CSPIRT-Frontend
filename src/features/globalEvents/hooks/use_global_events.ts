import { useQuery } from "@tanstack/react-query";
import { globalEventApi } from "../../../entities/globalEvents/api/global_event_api";

export const useGlobalEvents = () => {
    return useQuery({
        queryKey: ["global_events"],
        queryFn: () => globalEventApi.getGlobalEvents(),
    });
};