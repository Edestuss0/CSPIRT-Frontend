import {useMutation, useQueryClient} from "@tanstack/react-query";
import {type addUserValues} from "../models/add_user_usecase.ts";
import {UpdateUserUseCase} from "../models/update_user_usecase.ts";

export const useUpdateUser = () => {
    const queryclient = useQueryClient();

    return useMutation({
        mutationFn: (form: addUserValues) => UpdateUserUseCase(form),
        onSuccess: async () => {
            await queryclient.invalidateQueries({queryKey: ["users_by_class"]});
            await queryclient.invalidateQueries({queryKey: ["staff"]});
        }
    })
}