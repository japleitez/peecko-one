import dayjs from 'dayjs/esm';
import { IVideo } from 'app/entities/video/video.model';
import { IArticle } from 'app/entities/article/article.model';
import { CoachType } from 'app/entities/enumerations/coach-type.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface ICoach {
  id: number;
  type?: keyof typeof CoachType | null;
  name?: string | null;
  email?: string | null;
  website?: string | null;
  instagram?: string | null;
  phoneNumber?: string | null;
  country?: string | null;
  speaks?: string | null;
  resume?: string | null;
  notes?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  videos?: IVideo[] | null;
  articles?: IArticle[] | null;
}

export type NewCoach = Omit<ICoach, 'id'> & { id: null };

export interface CoachAccess {
  id: FieldAccess;
  type: FieldAccess;
  name: FieldAccess;
  email: FieldAccess;
  website: FieldAccess;
  instagram: FieldAccess;
  phoneNumber: FieldAccess;
  country: FieldAccess;
  speaks: FieldAccess;
  resume: FieldAccess;
  notes: FieldAccess;
  created: FieldAccess;
  updated: FieldAccess;
  videos: FieldAccess;
  articles: FieldAccess;
}

export let COACH_ACCESS: CoachAccess;

COACH_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  type: { listable: true, visible: true, disabled: false },
  name: { listable: true, visible: true, disabled: false },
  email: { listable: false, visible: true, disabled: false },
  website: { listable: false, visible: true, disabled: false },
  instagram: { listable: true, visible: true, disabled: false },
  phoneNumber: { listable: false, visible: true, disabled: false },
  country: { listable: true, visible: true, disabled: false },
  speaks: { listable: true, visible: true, disabled: false },
  resume: { listable: false, visible: true, disabled: false },
  notes: { listable: false, visible: true, disabled: false },
  created: { listable: false, visible: true, disabled: true },
  updated: { listable: false, visible: true, disabled: true },
  videos: { listable: false, visible: true, disabled: false },
  articles: { listable: false, visible: true, disabled: false },
};
