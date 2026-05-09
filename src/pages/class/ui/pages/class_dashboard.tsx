import {useEffect, useState} from "react";
import {useNavigate, useParams, useSearchParams} from "react-router-dom";
import {useClassDashboardStore} from "../../store/class_dashboard_store.ts";
import {useAuthStore} from "../../../../features/auth/store/auth_store.ts";
import {ChangeTeacherModal} from "../components/change_teacher_modal.tsx";
import {ConfirmModal} from "../../../../shared/ui/modals/confirm_modal.tsx";
import {UsersWidget} from "../../../../features/users/ui/components/users_widget.tsx";
import {NotesWidget} from "../../../../features/notes/ui/components/notes_widget.tsx";
import {ComplaintsWidget} from "../../../../features/complaints/ui/components/complaints_widget.tsx";
import {ScheduleWidget} from "../../../../features/schedule/ui/components/schedule_widget.tsx";
import {BaseScheduleWidget} from "../../../../features/schedule/ui/components/base_schedule_widget.tsx";

type SelectedList = | "users" | "notes" | "complaints" | "schedule" | "baseschedule";

export function ClassDashboard() {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    
    const teacher = useClassDashboardStore((state) => state.teacher);
    const { id } = useParams<{id: string, name: string}>();
    const name = searchParams.get("name");
    const classId = id ? parseInt(id, 10) : null;
    const role = useAuthStore((state) => state.user?.User.Role);
    const status = useClassDashboardStore((state) => state.status);
    const error = useClassDashboardStore((state) => state.error);
    const staff = useClassDashboardStore((state) => state.staff);
    const changeTeacher = useClassDashboardStore((state) => state.changeTeacher);
    const getStaff = useClassDashboardStore((state) => state.getStaff);
    const getClassTeacher = useClassDashboardStore((state) => state.getClassTeacher);
    const deleteClass = useClassDashboardStore((state) => state.deleteClass);
    const rolloverSchedule = useClassDashboardStore((state) => state.rolloverSchedule);
    
    const isLoading = status === "loading";
    
    const [selectedList, setSelectedList] = useState<SelectedList>("users"); 
    const [isChangeTeacherModalOpen, setChangeTeacherModalOpen] = useState(false);
    const [isDeleteClassModalOpen, setDeleteClassModalOpen] = useState(false);
    const [isRolloverModalOpen, setRolloverModalOpen] = useState(false);
    const [key, setKey] = useState<number>(0);

    useEffect(() => {
        if (classId) {
            void getClassTeacher(classId);
        }
    }, [classId, getClassTeacher]);

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
                            onClick={async () => setSelectedList('schedule')}
                            disabled={(selectedList === "schedule")}
                        >
                            Расписание класса
                        </button>

                        {role === "Owner" && (
                            <button
                                className={"btn btn--secondary"}
                                type={"button"}
                                onClick={() => setSelectedList('baseschedule')}
                                disabled={(selectedList === "baseschedule")}
                            >
                                Стандартное расписание
                            </button>
                        )}

                        {(role === "Admin" || role === "Owner" || role === "Helper") && (
                                <button
                                    className={"btn btn--secondary"}
                                    type={"button"}
                                    onClick={() => setSelectedList('notes')}
                                    disabled={(selectedList === "notes")}
                                >
                                    Список заметок класса
                                </button>
                        )}

                        {(role === "Admin" || role === "Owner") && (<button
                            className={"btn btn--secondary"}
                            type={"button"}
                            onClick={() => setSelectedList('complaints')}
                            disabled={(selectedList === "complaints")}
                        >
                            Список жалоб класса
                        </button>)}

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
                
                {selectedList === "users" && (
                    <UsersWidget name={name ?? ""} id={classId ?? 0} key={key} />
                )}
                
                {selectedList === "notes" && (
                    <NotesWidget id={classId ?? 0} name={name ?? ""} key={key} />
                )}

                {selectedList === "complaints" && (
                    <ComplaintsWidget id={classId ?? 0} name={name ?? ""} key={key} />
                )}

                {selectedList === "schedule" && (
                    <ScheduleWidget id={classId ?? 0} name={name ?? ""} key={key} />
                )}

                {selectedList === "baseschedule" && (
                    <BaseScheduleWidget id={classId ?? 0} name={name ?? ""} key={key} />
                )}
                
            </section>
            
            <ChangeTeacherModal isOpen={isChangeTeacherModalOpen} onClose={() => setChangeTeacherModalOpen(false)} onChangeTeacher={async (dto) => {
                if (classId !== null) {
                    await changeTeacher(classId, dto);
                    await getClassTeacher(classId);
                    setKey(key + 1);
                } 
                setChangeTeacherModalOpen(false);
            }} staff={staff} className={name ?? ""}/>
            
            <ConfirmModal
                title={"Удалить класс?"}
                content={`Это действие удалит мероприятие ${name} класс. Отменить удаление будет нельзя.`}
                onConfirm={async () => {
                    if (classId !== null) {
                        await deleteClass(classId);
                        setKey(key + 1);
                        setDeleteClassModalOpen(false);
                    }
                }}
                isOpen={isDeleteClassModalOpen}
                onClose={() => setDeleteClassModalOpen(false)}
                buttonContent={"Удалить"}
                isDanger={true}
            />

            <ConfirmModal
                title={"Сброс расписания"}
                content={"Вы уверены что хотите сбросить текущее расписание на стандартное?"}
                buttonContent={"Сбросить"}
                onConfirm={async () => {
                    if (classId) {
                        await rolloverSchedule(classId ?? 0);
                        setKey(key + 1);
                    }
                }}
                isOpen={isRolloverModalOpen}
                onClose={() => setRolloverModalOpen(false)}
            />
            
        </main>
    );
}
