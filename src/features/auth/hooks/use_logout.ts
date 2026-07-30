import {useMutation, useQueryClient} from "@tanstack/react-query";
import {useAuthStore} from "../store/auth_store.ts";
import {UserApi} from "../../../entities/user/api/user_api.ts";

export const useLogout = () => {
    const queryclient = useQueryClient();
    return useMutation({
        mutationFn: () => UserApi.logout(),
        onSuccess: () => {
            useAuthStore.setState({
                user: null,
                status: "unauthenticated",
                error: null,
            });
            queryclient.invalidateQueries();
        }
    })
}