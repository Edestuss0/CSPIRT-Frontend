import {z} from "zod";
import {
    type addUserFormType, type AuthDto,
    authDtoSchema, errorResponseSchema, loginResponseSchema, meResponseSchema, type meType, refreshResponseSchema,
    type updateUserFormType,
    userSchema,
    type UserType
} from "../types/user_types.ts";
import {noteSchema} from "../../notes/types/notes_types.ts";
import {complaintSchema} from "../../complaints/types/complaints_types.ts";
import {EventSchema} from "../../events/types/events_types.ts";
import {apiClient} from "../../../shared/api/client.ts";

const getUserResponseSchema = z.object({
    User: userSchema,
    Notes: z.array(noteSchema).optional(),
    Complaints: z.array(complaintSchema).optional(),
    Events: z.array(EventSchema).optional(),
    ClassTeacher: userSchema.nullable().optional(),
});

export type GettedUser = z.infer<typeof getUserResponseSchema>;

export const UserApi = {
    async getUser(id: number): Promise<GettedUser> {
        const response = await apiClient.get(`/api/users?id=${id}`, true);
        
        if (!response.checkStatus()) {
            throw new Error("Ошибка при получении данных о пользователе");
        }
        
        const parsed = getUserResponseSchema.safeParse(response.data);
        
        if (!parsed.success) {
            throw new Error("Некорректный формат пользователя");
        }
        
        return parsed.data
    },
    
    async getStaff(): Promise<UserType[]> {
        const response = await apiClient.get("/api/users/get/staff", true);
        
        if (!response.checkStatus()) {
            throw new Error("Ошибка при получении персонала");
        }
        
        const parsed = z.array(userSchema).safeParse(response.data);
        
        if (!parsed.success) {
            throw new Error("Некорректный ответ сервера");
        }
        
        return parsed.data
    },
    
    async updateUser(form: updateUserFormType): Promise<void> {
        const response = await apiClient.patch(`/api/user/update?id=${form.Id}`, {
            Avatar: {
                Valid: true,
                String: form.Avatar,
            },
            Name: form.Name,
            LastName: form.LastName,
            ClassID: form.ClassID,
            Login: form.Login,
            Role: form.Role,
            Rating: form.Rating,
            Class: form.Class,
        }, true);

        if (!response.checkStatus()) {
            throw new Error("Ошибка при попытке обновлении пользователя");
        }
    },
    
    async addUser(form: addUserFormType): Promise<boolean> {
      const response = await apiClient.patch("/api/user/add", {
          Avatar: {
            String: form.Avatar,
            Valid: true,  
          },
          Name: form.Name,
          LastName: form.LastName,
          FullName: form.FullName,
          Password: form.Password,
          ClassID: form.ClassID,
          Login: form.Login,
          Role: form.Role,
          Rating: 100,
      }, true);  
      
      if (!response.checkStatus()) {
          throw new Error("Ошибка при добавлении пользователя");
      }
      
      return true;
    },
    
    async deleteUser(id: number): Promise<boolean> {
        const response = await apiClient.delete(`/api/user/delete/${id}`, {}, true);
        
        if (!response.checkStatus()) {
            throw new Error("Ошибка при попытке удаления пользователя");
        }
        
        return true;
    },

    async login(dto: AuthDto): Promise<string> {
        const parsedDto = authDtoSchema.safeParse(dto);

        if (!parsedDto.success) {
            throw new Error("Некорректный логин или пароль");
        }

        const response = await apiClient.post<unknown>("/login", {
            Login: parsedDto.data.login,
            Password: parsedDto.data.password,
        });

        if (!response.checkStatus()) {
            throw new Error(getSafeAuthError(response.data));
        }

        const parsed = loginResponseSchema.safeParse(response.data);

        if (!parsed.success) {
            throw new Error("Некорректный ответ сервера авторизации");
        }

        return parsed.data.accessToken;
    },

    async refresh(): Promise<string> {
        const response = await apiClient.post<unknown>("/api/refresh");

        if (!response.checkStatus()) {
            throw new Error("Не удалось обновить сессию");
        }

        const parsed = refreshResponseSchema.safeParse(response.data);

        if (!parsed.success) {
            throw new Error("Некорректный ответ refresh");
        }

        return parsed.data.token;
    },

    async checkAuth(): Promise<meType> {
        const response = await apiClient.get<unknown>("/api/me", true);

        if (!response.checkStatus()) {
            throw new Error("Сессия недействительна");
        }

        const parsedUser = meResponseSchema.safeParse(response.data);

        if (!parsedUser.success) {
            throw new Error("Некорректный формат профиля");
        }

        return parsedUser.data;
    },

    async logout() {
        const response = await apiClient.patch("/api/user/logout", {}, true);

        if (!response.checkStatus()) {
            throw new Error("Ошибка при попытке выхода из аккаунта");
        }
    }
}

function getSafeAuthError(data: unknown): string {
    const parsed = errorResponseSchema.safeParse(data);

    if (!parsed.success) {
        return "Ошибка авторизации";
    }

    return parsed.data.error || parsed.data.message || "Ошибка авторизации";
}