import {useEffect, useState} from "react";
import {useNavigate, useParams, useSearchParams} from "react-router-dom";
import {UserCard} from "../../../../shared/ui/cards/user_card.tsx";
import {NoteCard} from "../../../../shared/ui/cards/note_card.tsx";
import {useClassDashboardStore} from "../../store/class_dashboard_store.ts";
import {ComplaintCard} from "../../../../shared/ui/cards/complaint_card.tsx";
import {useAuthStore} from "../../../auth/store/auth_store.ts";
import {ChangeTeacherModal} from "../components/change_teacher_modal.tsx";
import {ConfirmModal} from "../../../../shared/ui/modals/confirm_modal.tsx";
import {ScheduleDayCard} from "../../../../shared/ui/cards/schedule_day_card.tsx";

type SelectedList = | "users" | "notes" | "complaints" | "schedule" | "baseschedule";

export function ClassDashboard() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    
    const teacher = useClassDashboardStore((state) => state.teacher);
    const { id } = useParams<{id: string, name: string}>();
    const name = searchParams.get("name");
    const classId = id ? parseInt(id, 10) : null;
    const role = useAuthStore((state) => state.user?.User.Role);
    
    const getUsers = useClassDashboardStore((state) => state.getUsersByClass);
    const users = useClassDashboardStore((state) => state.users);
    const status = useClassDashboardStore((state) => state.status);
    const error = useClassDashboardStore((state) => state.error);
    const notes = useClassDashboardStore((state) => state.notes);
    const staff = useClassDashboardStore((state) => state.staff);
    const getNotes = useClassDashboardStore((state) => state.getNotesByClass);
    const deleteNote = useClassDashboardStore((state) => state.deleteNote);
    const complaints = useClassDashboardStore((state) => state.complaints);
    const schedule = useClassDashboardStore((state) => state.schedule);
    const getComplaints = useClassDashboardStore((state) => state.getComplaints);
    const deleteComplaint = useClassDashboardStore((state) => state.deleteComplaint);
    const changeTeacher = useClassDashboardStore((state) => state.changeTeacher);
    const getStaff = useClassDashboardStore((state) => state.getStaff);
    const getClassTeacher = useClassDashboardStore((state) => state.getClassTeacher);
    const deleteClass = useClassDashboardStore((state) => state.deleteClass);
    const getSchedule = useClassDashboardStore((state) => state.getClassSchedule);
    const rolloverSchedule = useClassDashboardStore((state) => state.rolloverSchedule);
    
    const isLoading = status === "loading";
    
    const [selectedList, setSelectedList] = useState<SelectedList>("users"); 
    const [isChangeTeacherModalOpen, setChangeTeacherModalOpen] = useState(false);
    const [isDeleteClassModalOpen, setDeleteClassModalOpen] = useState(false);
    const [isRolloverModalOpen, setRolloverModalOpen] = useState(false);
        
    useEffect(() => {
        if (classId !== null) {
            void getUsers(classId);
            void getClassTeacher(classId);
        }
    }, [getUsers, id, getClassTeacher])

    return (
        <main className={"main"}>
            <section className={"page"}>
                <div className={"profile-hero"}>
                    <div className={"info-row"}>
                        <h1 className={"info-row__value"}>{name} Класс</h1>
                        <h2 className={"info-row__label"}>Классный руководитель - {teacher?.Name} {teacher?.LastName}</h2>
                        {role === "Owner" &&  (
                            <div className="btn-group">
                                <button className={"btn btn--primary"} onClick={async () => {
                                    await getStaff();
                                    setChangeTeacherModalOpen(!isChangeTeacherModalOpen);
                                }}>Изменить классного руководителя
                                </button>
                                <button className={"btn btn--danger"} onClick={async () => {
                                    await getStaff();
                                    setDeleteClassModalOpen(!isDeleteClassModalOpen)
                                }}>Удалить класс
                                </button>
                            </div>
                        )}
                    </div>
                    <div className={"btn-group"}>
                        <button
                            className={"btn btn--secondary"}
                            type={"button"}
                            onClick={() => setSelectedList('users')}
                            disabled={(selectedList === "users")}
                        >
                            Список учеников
                        </button>

                        <button
                            className={"btn btn--secondary"}
                            type={"button"}
                            onClick={async () => {
                                if (classId) {
                                    await getSchedule(classId, "current");
                                    setSelectedList('schedule');
                                }
                            }}
                            disabled={(selectedList === "schedule")}
                        >
                            Расписание класса
                        </button>

                        {role === "Owner" && (
                            <button
                                className={"btn btn--secondary"}
                                type={"button"}
                                onClick={() => {
                                    if (classId !== null) {
                                        void getSchedule(classId, "base");
                                        setSelectedList('baseschedule');
                                    }
                                }}
                                disabled={(selectedList === "baseschedule")}
                            >
                                Стандартное расписание
                            </button>
                        )}

                        {(role === "Admin" || role === "Owner" || role === "Helper") && (
                                <button
                                    className={"btn btn--secondary"}
                                    type={"button"}
                                    onClick={() => {
                                        if (classId !== null) {
                                            void getNotes(classId);
                                            setSelectedList('notes');
                                        }
                                    }}
                                    disabled={(selectedList === "notes")}
                                >
                                    Список заметок класса
                                </button>
                        )}

                        <button
                            className={"btn btn--secondary"}
                            type={"button"}
                            onClick={() => {
                                if (classId !== null) {
                                    setSelectedList('complaints');
                                    void getComplaints(classId);
                                }
                            }}
                            disabled={(selectedList === "complaints")}
                        >
                            Список жалоб класса
                        </button>

                        {role === "Owner" && (
                            <button
                                className={"btn btn--primary"}
                                type="button"
                                onClick={() => setRolloverModalOpen(true)}
                            >
                                Сбросить расписание
                            </button>
                        )}
                        
                        <button
                            className={"btn btn--primary"}
                            type="button"
                            onClick={() => {
                                navigate("/");
                            }}
                        >
                            На главную
                        </button>
                    </div>
                </div>
                
                <div className="page-spacer"></div>

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
                
                {selectedList === "users" ? users.length > 0 ? (
                    <div className={"class-list"}>
                        {users.map((user) => (
                            <UserCard user={user} key={user.Id} onClick={() => navigate(`/user/${user.Id}`)}/>
                        ))}
                    </div>
                ) : (
                    !isLoading && <div className="empty-state">
                        <h2 className="empty-state__title">Ученики не найдены</h2>
                        <p className="empty-state__text">
                            Не удалось найти учеников по {name} классу
                        </p>
                    </div>
                ) : <div></div>}

                    
                
                {selectedList === "notes" && notes.length > 0 ? (
                    <div className={"class-list"}>
                        {notes.map((note) => (
                            <NoteCard item={note} key={note.ID} onDelete={async () => {
                                await deleteNote(note.ID);
                                if (classId !== null) {
                                    getNotes(classId);
                                }
                            }} role={role ?? "User"} />
                        ))}
                    </div>
                ) :(
                    selectedList === "notes" && !isLoading && <div className="empty-state">
                        <h2 className="empty-state__title">Заметки не найдены</h2>
                        <p className="empty-state__text">
                            Не удалось найти заметки по {name} классу
                        </p>
                    </div>
                )}

                {selectedList === "complaints" && complaints.length > 0 ? (
                    <div className={"class-list"}>
                        {complaints.map((item) => (
                            <ComplaintCard item={item} key={item.ID} onDelete={() => {
                                deleteComplaint(item.ID);
                                if (classId !== null) {
                                    getComplaints(classId);
                                }
                            }} role={role ?? "User"} />
                        ))}
                    </div>
                ) :(
                    selectedList === "complaints" && !isLoading && <div className="empty-state">
                        <h2 className="empty-state__title">Жалобы не найдены</h2>
                        <p className="empty-state__text">
                            Не удалось найти жалобы по {name} классу
                        </p>
                    </div>
                )}

                {selectedList === "schedule" && schedule && (
                    <div className="schedule-days-list">
                        <ScheduleDayCard title="Понедельник" lessons={schedule.monday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "current") }} classId={classId ?? 0} day={"monday"}  type={"current"} />
                        <ScheduleDayCard title="Вторник" lessons={schedule.tuesday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "current") }}  classId={classId ?? 0} day={"tuesday"} type={"current"} />
                        <ScheduleDayCard title="Среда" lessons={schedule.wednesday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "current") }} classId={classId ?? 0} day={"wednesday"} type={"current"} />
                        <ScheduleDayCard title="Четверг" lessons={schedule.thursday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "current") }} classId={classId ?? 0} day={"thursday"} type={"current"} />
                        <ScheduleDayCard title="Пятница" lessons={schedule.friday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "current") }} classId={classId ?? 0} day={"friday"} type={"current"} />
                    </div>
                )}

                {selectedList === "schedule" && !isLoading && !schedule && (
                    <div className="empty-state">
                        <h2 className="empty-state__title">Расписание не найдено</h2>
                        <p className="empty-state__text">
                            Не удалось найти расписание для {name} класса
                        </p>
                    </div>
                )}

                {selectedList === "baseschedule" && schedule && (
                    <div className="schedule-days-list">
                        <ScheduleDayCard title="Понедельник" lessons={schedule.monday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "base") }} classId={classId ?? 0} day={"monday"}  type={"base"} />
                        <ScheduleDayCard title="Вторник" lessons={schedule.tuesday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "base") }}  classId={classId ?? 0} day={"tuesday"} type={"base"} />
                        <ScheduleDayCard title="Среда" lessons={schedule.wednesday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "base") }} classId={classId ?? 0} day={"wednesday"} type={"base"} />
                        <ScheduleDayCard title="Четверг" lessons={schedule.thursday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "base") }} classId={classId ?? 0} day={"thursday"} type={"base"} />
                        <ScheduleDayCard title="Пятница" lessons={schedule.friday ?? []} onChangeScheduleLesson={async () => {if (classId) await getSchedule(classId, "base") }} classId={classId ?? 0} day={"friday"} type={"base"} />
                    </div>
                )}

                {selectedList === "baseschedule" && !isLoading && !schedule && (
                    <div className="empty-state">
                        <h2 className="empty-state__title">Расписание не найдено</h2>
                        <p className="empty-state__text">
                            Не удалось найти стандартное расписание для {name} класса
                        </p>
                    </div>
                )}
                
            </section>
            <ChangeTeacherModal isOpen={isChangeTeacherModalOpen} onClose={() => setChangeTeacherModalOpen(false)} onChangeTeacher={async (dto) => {
                if (classId !== null) {
                    await changeTeacher(classId, dto);
                    await getUsers(classId);
                    await getClassTeacher(classId);
                } 
                setChangeTeacherModalOpen(false);
            }} staff={staff} className={name ?? ""}/>


            {isDeleteClassModalOpen && (
                <ConfirmModal
                    title={"Удалить класс?"}
                    content={`Это действие удалит мероприятие ${name} класс. Отменить удаление будет нельзя.`}
                    onConfirm={async () => {
                        if (classId !== null) {
                            await deleteClass(classId);
                            setDeleteClassModalOpen(false);
                            navigate(-1);
                        }
                    }}
                    isOpen={isDeleteClassModalOpen}
                    onClose={() => setDeleteClassModalOpen(false)}
                    buttonContent={"Удалить"}
                    isDanger={true}
                />
            )}

            <ConfirmModal
                title={"Сброс расписания"}
                content={"Вы уверены что хотите сбросить текущее расписание на стандартное?"}
                buttonContent={"Сбросить"}
                onConfirm={async () => {
                    if (classId) {
                        await rolloverSchedule(classId);
                        await getSchedule(classId, "current");
                    }
                }}
                isOpen={isRolloverModalOpen}
                onClose={() => setRolloverModalOpen(false)}
            />
            
        </main>
    );
}
