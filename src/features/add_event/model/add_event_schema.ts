import {z} from "zod";

export const addEventFormSchema = z.object({
    Title: z.string(),
    Description: z.string(),
    StartedAt: z.string(),
    Classes: z.array(z.number().int().positive()),
    RatingReward: z.number().int().positive(),
});