import {z} from "zod";

export const addGlobalInfoEventSchema = z.object({
    title: z.string().min(4).max(64),
    description: z.string().min(10).max(1000),
});

export const addGlobalQuizEventSchema = z.object({
    title: z.string().min(4).max(64),
    description: z.string().min(10).max(1000),
    options: z.array(z.object({title: z.string(), votes: z.number().int().nonnegative()})),
});

export const voteGlobalQuizSchema = z.object({
    voteItemId: z.number().int().nonnegative(),
})