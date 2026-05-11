import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../../../features/auth/store/auth_store.ts";
import { AddUserModal } from "../../../../features/users/ui/components/add_user_modal.tsx";
import {AddClassModal} from "../../../../features/class/ui/components/add_class_modal.tsx";
import {AddEventModal} from "../../../../features/events/ui/components/add_event_modal.tsx";
import {ClassesWidget} from "../../../../features/class/ui/components/classes_widget.tsx";
import {EventsWidget} from "../../../../features/events/ui/components/events_widget.tsx";
import {StaffWidget} from "../../../../features/users/ui/components/staff_widget.tsx";
import {type BurgerDrawerMenuItem} from "../../../../shared/ui/other/burger_menu.tsx";
import {PageHeader} from "../../../../shared/ui/other/page_header.tsx";
import {TabsSwitcher, type TabsSwitcherItem} from "../../../../shared/ui/other/tabs_switcher.tsx";

type Lists = "classes" | "events" | "staff";

export function DashboardPage() {
    const navigate = useNavigate();

    const role = useAuthStore((state) => state.user?.User.Role);
    
    const [selectedList, setSelectedList] = useState<Lists>("classes");
    const [isAddUserModalOpen, setIsAddUserModalOpen] = useState(false);
    const [isAddClassModalOpen, setIsAddClassModalOpen] = useState(false);
    const [isAddEventModalOpen, setIsAddEventModalOpen] = useState(false);
    const [key, setKey] = useState(0);
    
    const menuItems: BurgerDrawerMenuItem[] = [
        {
            label: "Добавить пользователя",
            onClick: () => setIsAddUserModalOpen(true),
            hidden: (role !== "Owner" || selectedList !== "classes"),
        },
        {
            label: "Добавить класс",
            onClick: () => setIsAddClassModalOpen(true),
            hidden: (role !== "Owner" || selectedList !== "classes"),
        },
        {
            label: "Добавить мероприятие",
            onClick: () => setIsAddEventModalOpen(true),
            hidden: (role !== "Owner" || selectedList !== "events"),
        },
        {
            label: "Профиль",
            onClick: () => navigate("/profile")
        },
    ]
    
    const tabs: TabsSwitcherItem<Lists>[] = [
        {
            label: "Классы",
            value: "classes",
            disabled: selectedList === "classes"
        },
        {
            value: "events",
            label: "Мероприятия",
            disabled: selectedList === "events"
        },
        {
            label: "Персонал",
            value: "staff",
            disabled: selectedList === "staff",
            hidden: role !== "Owner"
        }
    ]

    if (!role) {
        return null;
    }

    return (
        <main className="main">
            <section className="page">
                
                <PageHeader
                    title={"Главная страница"}
                    description={"Просматривайте список классов, мероприятий и прочее"}
                    menuItems={menuItems}
                    menuTitle={"Меню"}
                />

                <div className="page-spacer" />
                
                <TabsSwitcher
                    items={tabs}
                    value={selectedList}
                    onChange={setSelectedList}
                />
                
                <div className="page-spacer" />
                

                {selectedList === "classes" && (
                    <ClassesWidget key={key}/>
                )}

                {selectedList === "events" && (
                    <EventsWidget key={key}/>
                )}

                {selectedList === "staff" && (
                    <StaffWidget key={key}/>
                )}

                <AddUserModal
                    isOpen={isAddUserModalOpen}
                    onClose={() => setIsAddUserModalOpen(false)}
                    onAddUser={async () => {
                        setKey(prevKey => prevKey + 1);
                        setIsAddUserModalOpen(false);
                    }}
                />
                
                <AddClassModal isOpen={isAddClassModalOpen} onClose={() => setIsAddClassModalOpen(false)} onAddClass={async () => {
                    setKey(prevKey => prevKey + 1);
                    setIsAddClassModalOpen(false);
                }}/>
                
                <AddEventModal isOpen={isAddEventModalOpen} onClose={() => setIsAddEventModalOpen(false)} onEventAdd={async () => {
                 setKey(prevKey => prevKey + 1);
                 setIsAddEventModalOpen(false);
                }} />
            </section>
        </main>
    );
}
