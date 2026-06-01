import type {UserRole} from "../../../shared/entities/user/types/user_types.ts";
import {UserApi} from "../../../shared/entities/user/api/user_api.ts";
import {updateUserFormSchema} from "./update_user_schena.ts";

export type updateUserValues = {
    avatar: string;
    name: string;
    lastname: string;
    id: number;
    classId: number;
    login: string;
    role: UserRole;
    rating: number;
}

export async function UpdateUserUseCase(form: updateUserValues): Promise<boolean> {

    const dto = {
        Id: form.id,
        Avatar: form.avatar,
        Name: form.name,
        LastName: form.lastname,
        ClassID: form.classId,
        Login: form.login,
        Role: form.role,
        Rating: form.rating,
    }
    const parsed = updateUserFormSchema.safeParse(dto);

    if (!parsed.success) {
        throw new Error(JSON.stringify(parsed.error?.format()));
    }

    await UserApi.updateUser(parsed.data);

    return true;
}