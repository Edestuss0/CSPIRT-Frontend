import {useEffect, useState} from "react";
import {useParams, useSearchParams} from "react-router-dom";
import {useClassDashboardStore} from "../../store/class_dashboard_store.ts";
import {useAuthStore} from "../../../../features/auth/store/auth_store.ts";
import {ChangeTeacherModal} from "../components/change_teacher_modal.tsx";
import {ConfirmModal} from "../../../../shared/ui/modals/confirm_modal.tsx";
import {UsersWidget} from "../../../../features/users/ui/components/users_widget.tsx";
import {NotesWidget} from "../../../../features/notes/ui/components/notes_widget.tsx";
import {ComplaintsWidget} from "../../../../features/complaints/ui/components/complaints_widget.tsx";
import {ScheduleWidget} from "../../../../features/schedule/ui/components/schedule_widget.tsx";
import {BaseScheduleWidget} from "../../../../features/schedule/ui/components/base_schedule_widget.tsx";
// import {PlannedScheduleWidget} from "../../../../features/schedule/ui/components/planned_schedule_widget.tsx";
import {type BurgerDrawerMenuItem} from "../../../../shared/ui/other/burger_menu.tsx";
import {PageHeader} from "../../../../shared/ui/other/page_header.tsx";
import {TabsSwitcher, type TabsSwitcherItem} from "../../../../shared/ui/other/tabs_switcher.tsx";

type SelectedList = | "users" | "notes" | "complaints" | "schedule" | "baseschedule" | "plannedschedule";

export function ClassDashboard() {
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

    const menuItems: BurgerDrawerMenuItem[] = [
        {
            label: "Изменить классного руководителя",
            onClick: async () => {
                await getStaff();
                setChangeTeacherModalOpen(true);
            },
            hidden: role !== "Owner",
        },
        {
            label: "Удалить класс",
            onClick: () => setDeleteClassModalOpen(true),
            hidden: role !== "Owner",
        },
        {
            label: "Сбросить расписание",
            onClick: () => setRolloverModalOpen(true),
            hidden: role !== "Owner",
        },
    ];
    
    const tabs: TabsSwitcherItem<SelectedList>[] = [
        {
            value: "users",
            label: "Ученики",
        },
        {
            value: "schedule",
            label: "Расписание",
        },
        {
            value: "baseschedule",
            label: "Стандартное расписание",
            hidden: role !== "Owner",
        },
        {
            value: "notes",
            label: "Заметки",
            hidden: !(role === "Admin" || role === "Owner" || role === "Helper"),
        },
        {
            value: "complaints",
            label: "Жалобы",
            hidden: !(role === "Admin" || role === "Owner"),
        },
    ]

    return (
        <main className={"main"}>
            <section className={"page"}>
                
                <PageHeader 
                    title={`${name} Класс`}
                    description={`Классный руководитель - ${teacher?.Name ?? ""} ${teacher?.LastName ?? ""}`}
                    menuItems={menuItems}
                    hasBackButton={true}
                />
                
                <div className="page-spacer"></div>
                
                <TabsSwitcher
                    items={tabs}
                    value={selectedList}
                    onChange={setSelectedList}
                />

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

                {/*{selectedList === "plannedschedule" && (*/}
                {/*    <PlannedScheduleWidget id={classId ?? 0} name={name ?? ""} key={key} />*/}
                {/*)}*/}
                
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
                content={`Это действие удалит ${name} класс. Отменить удаление будет нельзя.`}
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
