import {fullNameSchema} from "../../../../shared/entities/user/types/user_types.ts";
import {z} from "zod";

export const addUserFormSchema = z.object({
    Name: z.string().min(2).max(20),
    LastName: z.string().min(2).max(20),
    FullName: z.array(fullNameSchema),
    Password: z.string().min(6).max(35),
    ClassID: z.number().int().nonnegative(),
    Login: z.string().min(2).max(20),
    Role: z.enum(["User", "Helper", "Admin", "Owner"]),
}).superRefine((data, ctx) => {
    const roleRequiresClass = data.Role === "User" || data.Role === "Helper";

    if (roleRequiresClass && data.ClassID <= 0) {
        ctx.addIssue({
            code: z.ZodIssueCode.custom,
            path: ["ClassID"],
            message: "Для ученика или старосты необходимо выбрать класс",
        });
    }
});