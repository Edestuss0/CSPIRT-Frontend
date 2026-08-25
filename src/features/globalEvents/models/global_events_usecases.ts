import {addGlobalInfoEventSchema, addGlobalQuizEventSchema} from "./schemas.ts";
import {globalEventApi} from "../../../entities/globalEvents/api/global_event_api.ts";


export type GlobalEventInfoAddForm = {
    title: string
    description: string
}

export async function addGlobalEventInfo(form: GlobalEventInfoAddForm) {
    const parsed = addGlobalInfoEventSchema.safeParse(form);
    if (!parsed.success) {
        throw new Error("Проверьте правильность заполнения формы");
    }
    
    await globalEventApi.addGlobalInfoEvent(parsed.data)
}

export type GlobalEventQuizAddForm = {
    title: string
    description: string
    options: {title: number, votes: number}[]
}

export async function addGlobalEventQuiz(form: GlobalEventQuizAddForm) {
    const parsed = addGlobalQuizEventSchema.safeParse(form);
    if (!parsed.success) {
        throw new Error("Проверьте правильность заполнения формы");
    }

    await globalEventApi.addGlobalQuizEvent(form)
}

export async function deleteGlobalEventInfo(id: number) {
    await globalEventApi.deleteGlobalInfoEvent(id)
}

export async function deleteGlobalEventQuiz(id: number) {
    await globalEventApi.deleteGlobalQuizEvent(id)
}

export type GlobalEventVoteForm = {
    voteItemId: number;
    id: number;
}

export async function voteGlobalEventQuiz(form: GlobalEventVoteForm) {
    const dto = {voteItemId: form.voteItemId};
    
    const parsed = addGlobalQuizEventSchema.safeParse(dto);
    if (!parsed.success) {
        throw new Error("Проверьте правильность заполнения формы");
    }

    await globalEventApi.voteGlobalQuizEvent(form.id, form)
}
