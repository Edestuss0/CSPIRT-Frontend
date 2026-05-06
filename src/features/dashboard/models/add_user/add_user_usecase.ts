import type {addUserFormType} from "../../../../shared/entities/user/types/user_types.ts";
import {addUserFormSchema} from "./add_user_chema.ts";
import {UserApi} from "../../../../shared/entities/user/api/user_api.ts";

export async function AddUserUseCase(form: addUserFormType) {
    const parsed = addUserFormSchema.safeParse(form);
    
    if (!parsed.success) {
        throw new Error(JSON.stringify(parsed.error?.format()));
    }
    
    await UserApi.addUser(form);
}