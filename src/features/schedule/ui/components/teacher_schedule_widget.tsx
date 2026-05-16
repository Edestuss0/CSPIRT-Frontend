import {ScheduleDayCard} from "../../../../shared/ui/cards/schedule_day_card.tsx";
import {useTeacherSchedule} from "../../hooks/use_teacher_schedule.ts";

type props = {
    name: string,
    id: number
}

export function TeacherScheduleWidget({name, id}: props) {
    const getSchedule = useTeacherSchedule(id);
    const schedule = getSchedule.data;
    const error = getSchedule.error?.message || null;
    const isLoading = getSchedule.isLoading;

    return (
        <>
            {isLoading && (
                <div className="grid grid--3">
                    <div className="skeleton" style={{ height: 160 }} />
                    <div className="skeleton" style={{ height: 160 }} />
                    <div className="skeleton" style={{ height: 160 }} />
                </div>
            )}

            {error && !isLoading && (
                <div className="alert alert--danger mb-4">{error}</div>
            )}

            {schedule ? (
                <div className="schedule-days-list">
                    <ScheduleDayCard title="Понедельник" lessons={schedule.monday ?? []} onChangeScheduleLesson={async () => getSchedule.refetch()} isTeacher={true} day={"monday"}  type={"current"} />
                    <ScheduleDayCard title="Вторник" lessons={schedule.tuesday ?? []} onChangeScheduleLesson={async () => getSchedule.refetch()} isTeacher={true} day={"tuesday"} type={"current"} />
                    <ScheduleDayCard title="Среда" lessons={schedule.wednesday ?? []} onChangeScheduleLesson={async () => getSchedule.refetch()} isTeacher={true} day={"wednesday"} type={"current"} />
                    <ScheduleDayCard title="Четверг" lessons={schedule.thursday ?? []} onChangeScheduleLesson={async () => getSchedule.refetch()} isTeacher={true} day={"thursday"} type={"current"} />
                    <ScheduleDayCard title="Пятница" lessons={schedule.friday ?? []} onChangeScheduleLesson={async () => getSchedule.refetch()} isTeacher={true} day={"friday"} type={"current"} />
                </div>
            ) : !isLoading && !schedule && (
                <div className="empty-state">
                    <h2 className="empty-state__title">Расписание не найдено</h2>
                    <p className="empty-state__text">
                        Не удалось найти расписание для {name}
                    </p>
                </div>
            )}

        </>
    );
} 