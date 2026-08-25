import { z } from "zod";
import {LOGIN_REGEX, SECURITY_LIMITS} from "../../../shared/lib/security/security_limits.ts";
import {noteSchema} from "../../notes/types/notes_types.ts";
import {complaintSchema} from "../../complaints/types/complaints_types.ts";
import {EventSchema} from "../../events/types/events_types.ts";

const nullableArray = <T extends z.ZodTypeAny>(schema: T) =>
    z.array(schema)
        .nullish()
        .transform((value) => value ?? []);

const roles = ["Owner", "Admin", "Helper", "User", "Public"] as const;

export const userRoleSchema = z
    .string()
    .transform((value) => {
        const lower = value.toLowerCase();

        switch (lower) {
            case "owner":
                return "Owner";
            case "admin":
                return "Admin";
            case "helper":
                return "Helper";
            case "user":
                return "User";
            case "public":
                return "Public";
            default:
                return value;
        }
    })
    .pipe(z.enum(roles));

export const fullNameSchema = z.object({
    Name: z.string(),
    LastName: z.string(),
});

export const userSchema = z.object({
    Id: z.number().int().nonnegative(),
    Name: z.string().max(100),
    LastName: z.string().max(100),
    Avatar: z.object({
        String: z.string(),
        Valid: z.boolean()
    }),
    FullName: nullableArray(fullNameSchema),
    Login: z.string().min(1).max(64),
    Rating: z.number().int(),
    Role: userRoleSchema,
    Class: z.string().max(32),
    ClassID: z.number().int().nonnegative(),
});

export type UserType = z.infer<typeof userSchema>;
export type UserRole = z.infer<typeof userRoleSchema>;

export const UserRoles: Record<UserRole, string> = {
    Admin: "Учитель",
    User: "Ученик",
    Owner: "Руководство",
    Helper: "Староста",
    Public: "Публичный"
};

export type addUserFormType = {
    Avatar: string;
    Name: string;
    LastName: string;
    FullName: Array<{
        Name: string;
        LastName: string;
    }>
    Password: string;
    ClassID: number;
    Login: string;
    Role: UserRole;
    Rating: number;
}

export type updateUserFormType = {
    Avatar: string;
    Name: string;
    LastName: string;
    ClassID: number;
    Login: string;
    Role: UserRole;
    Rating: number;
    Id: number;
    Class: string;
}

export interface AuthDto {
    login: string;
    password: string;
}

export const authDtoSchema = z.object({
    login: z
        .string()
        .min(SECURITY_LIMITS.loginMin)
        .max(SECURITY_LIMITS.loginMax)
        .regex(LOGIN_REGEX),
    password: z
        .string()
        .min(SECURITY_LIMITS.passwordMin)
        .max(SECURITY_LIMITS.passwordMax),
});

export const loginResponseSchema = z.object({
    accessToken: z.string().min(20).max(4096),
});

export const refreshResponseSchema = z.object({
    token: z.string().min(20).max(4096),
});

export const meResponseSchema = z.object({
    User: userSchema,
    Notes: z.array(noteSchema).optional().nullable(),
    Complaints: z.array(complaintSchema).optional().nullable(),
    Events: z.array(EventSchema).optional().nullable(),
    ClassTeacher: userSchema.optional().nullable().nullable(),
})

export const errorResponseSchema = z.object({
    error: z.string().optional(),
    message: z.string().optional(),
}).passthrough();

export type meType = z.infer<typeof meResponseSchema>