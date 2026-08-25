import {
    type GlobalEventInfoAddType,
    type GlobalEventOutput,
    GlobalEventOutputSchema, type GlobalEventQuizAddType, type GlobalEventVoteType
} from "../types/global_event_types.ts";
import {apiClient} from "../../../shared/api/client.ts";
import {z} from "zod";

export const globalEventApi = {
    async getGlobalEvents(): Promise<GlobalEventOutput> {
        const response = await apiClient.get("/api/events/global", true);
        if (!response.checkStatus()) {
            throw new Error("Ошибка при получении списка мероприятий");
        }
        const schema = z.array(GlobalEventOutputSchema).safeParse(response.data);
        if (!schema.success || schema.data[0] === undefined) {
            throw new Error("Некорректный ответ сервера");
        }
        return schema.data[0]
    },
    
    async addGlobalInfoEvent(body: GlobalEventInfoAddType) {
        const response = await apiClient.patch("/event/global/info/add", body, true);
        if (!response.checkStatus()) {
            throw new Error("Ошибка при добавлении мероприятия");
        }
        return;
    },

    async addGlobalQuizEvent(body: GlobalEventQuizAddType) {
        const response = await apiClient.patch("/event/global/quiz/add", body, true);
        if (!response.checkStatus()) {
            throw new Error("Ошибка при добавлении мероприятия");
        }
        return;
    },
    
    async deleteGlobalInfoEvent(id: number) {
        const response = await apiClient.delete(`/event/global/info/delete?Id=${id}`, {}, true);
        if (!response.checkStatus()) {
            throw new Error("Ошибка при удалении мероприятия");
        }
        return;
    },

    async deleteGlobalQuizEvent(id: number) {
        const response = await apiClient.delete(`/event/global/quiz/delete?Id=${id}`, {}, true);
        if (!response.checkStatus()) {
            throw new Error("Ошибка при удалении мероприятия");
        }
        return;
    },
    
    async voteGlobalQuizEvent(id: number, vote: GlobalEventVoteType) {
        const response = await apiClient.patch(`/event/global/quiz/${id}/vote`, vote, true);
        if (!response.checkStatus()) {
            throw new Error("Ошибка при голосовании");
        }
        return;
    }
}