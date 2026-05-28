import type {ParallelType} from "../../entities/class/types/class_types.ts";

interface Props {
    item: ParallelType;
    onClick?: () => void;
}

export function ParallelCard({ item, onClick }: Props) {
    const classesCount = item.ClassesIds?.length ?? 0;

    return (
        <button
            className="class-flat-card"
            type="button"
            onClick={onClick}
        >
            <div className="class-flat-card__main">
                <div className="class-flat-card__info">
                    <h2 className="class-flat-card__title">{item.Name}</h2>

                    <p className="class-flat-card__subtitle">
                        Количество классов: {classesCount}
                    </p>
                </div>
            </div>
        </button>
    );
}