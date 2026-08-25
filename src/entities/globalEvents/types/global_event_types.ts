import {z} from "zod";


const QuizOptionSchema = z.object({
    id: z.number().nonnegative(),
    title: z.string(),
    votes: z.number().nonnegative(),
});

const GlobalEventQuizSchema = z.object({
    id: z.number().nonnegative(),
    title: z.string(),
    description: z.string(),
    options: z.array(QuizOptionSchema),
});
 
const GlobalEventInfoSchema = z.object({
    id: z.number().nonnegative(),
    title: z.string(),
    description: z.string(),
})

export type GlobalEventQuiz = z.infer<typeof GlobalEventQuizSchema>;
export type GlobalEventInfo = z.infer<typeof GlobalEventInfoSchema>;

export const GlobalEventOutputSchema = z.object({
    info_events: z.array(GlobalEventInfoSchema),
    quizzes: z.array(GlobalEventQuizSchema),
})

export type GlobalEventOutput = z.infer<typeof GlobalEventOutputSchema>;

export type GlobalEventInfoAddType = {
    title: string
    description: string
}

export type GlobalEventQuizAddType = {
    title: string
    description: string
    options: {title: number, votes: number}[]
}

export type GlobalEventVoteType = {
    voteItemId: number;
}