import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAuthStore } from "../../../../features/auth/store/auth_store.ts";
import { AddUserModal } from "../../../../features/users/ui/components/add_user_modal.tsx";
import {AddClassModal} from "../../../../features/class/ui/components/add_class_modal.tsx";
import {AddEventModal} from "../../../../features/events/ui/components/add_event_modal.tsx";
import {ClassesWidget} from "../../../../features/class/ui/components/classes_widget.tsx";
import {EventsWidget} from "../../../../features/events/ui/components/events_widget.tsx";
import {StaffWidget} from "../../../../features/users/ui/components/staff_widget.tsx";

type Lists = "classes" | "events" | "staff";

export function DashboardPage() {
    const navigate = useNavigate();

    const role = useAuthStore((state) => state.user?.User.Role);
    
    const [selectedList, setSelectedList] = useState<Lists>("classes");
    const [isAddUserModalOpen, setIsAddUserModalOpen] = useState(false);
    const [isAddClassModalOpen, setIsAddClassModalOpen] = useState(false);
    const [isAddEventModalOpen, setIsAddEventModalOpen] = useState(false);
    const [key, setKey] = useState(0);

    if (!role) {
        return null;
    }

    return (
        <main className="main">
            <section className="page">
                <div className="profile-hero">
                    <div className="info-row">
                        <p className="info-row__label">
                            Главная панель системы
                        </p>
                    </div>

                    <div className="btn-group">
                        {selectedList === "classes" && role === "Owner" && (
                            <div className="btn-group">
                                <button
                                    className="btn btn--primary"
                                    type="button"
                                    onClick={() => setIsAddUserModalOpen(true)}
                                >
                                    Добавить пользователя
                                </button>

                                <button
                                    className="btn btn--primary"
                                    type="button"
                                    onClick={() => setIsAddClassModalOpen(!isAddClassModalOpen)}
                                >
                                    Добавить класс
                                </button>
                            </div>
                        )}

                        {selectedList === "events" && role === "Owner" && (<button
                            className="btn btn--primary"
                            type="button"
                            onClick={() => setIsAddEventModalOpen(true)}
                        >
                            Добавить мероприятие
                        </button>)}

                        <button
                            className="btn btn--secondary"
                            type="button"
                            onClick={() => setSelectedList("classes")}
                            disabled={selectedList === "classes"}
                        >
                            Классы
                        </button>

                        {role === "Owner" && (
                            <button
                                className="btn btn--secondary"
                                type="button"
                                onClick={() => setSelectedList("staff")}
                                disabled={selectedList === "staff"}
                            >
                                Персонал
                            </button>
                        )}

                        <button
                            className="btn btn--secondary"
                            type="button"
                            onClick={() => setSelectedList("events")}
                            disabled={selectedList === "events"}
                        >
                            Мероприятия
                        </button>

                        <button
                            className="btn btn--primary"
                            type="button"
                            onClick={() => navigate("/profile")}
                        >
                            Профиль
                        </button>
                    </div>
                </div>

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
