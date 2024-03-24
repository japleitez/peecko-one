import dayjs from 'dayjs/esm';
import { IVideoCategory } from 'app/entities/video-category/video-category.model';
import { ICoach } from 'app/entities/coach/coach.model';
import { Language } from 'app/entities/enumerations/language.model';
import { Player } from 'app/entities/enumerations/player.model';
import { Intensity } from 'app/entities/enumerations/intensity.model';
import { FieldAccess } from '../../shared/profile/view.models';

export interface IVideo {
  id: number;
  code?: string | null;
  title?: string | null;
  duration?: number | null;
  language?: keyof typeof Language | null;
  tags?: string | null;
  player?: keyof typeof Player | null;
  thumbnail?: string | null;
  url?: string | null;
  audience?: string | null;
  intensity?: keyof typeof Intensity | null;
  filename?: string | null;
  description?: string | null;
  created?: dayjs.Dayjs | null;
  released?: dayjs.Dayjs | null;
  archived?: dayjs.Dayjs | null;
  videoCategory?: IVideoCategory | null;
  coach?: ICoach | null;
}

export type NewVideo = Omit<IVideo, 'id'> & { id: null };

export interface VideoAccess {
  id: FieldAccess;
  code: FieldAccess;
  title: FieldAccess;
  duration: FieldAccess;
  language: FieldAccess;
  tags: FieldAccess;
  player: FieldAccess;
  thumbnail: FieldAccess;
  url: FieldAccess;
  audience: FieldAccess;
  intensity: FieldAccess;
  filename: FieldAccess;
  description: FieldAccess;
  created: FieldAccess;
  released: FieldAccess;
  archived: FieldAccess;
  videoCategory: FieldAccess;
  coach: FieldAccess;
}

export let VIDEO_ACCESS: VideoAccess;

VIDEO_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  title: { listable: true, visible: true, disabled: false },
  duration: { listable: true, visible: true, disabled: false },
  language: { listable: true, visible: true, disabled: false },
  tags: { listable: false, visible: true, disabled: false },
  player: { listable: true, visible: true, disabled: false },
  thumbnail: { listable: false, visible: true, disabled: false },
  url: { listable: false, visible: true, disabled: false },
  audience: { listable: false, visible: true, disabled: false },
  intensity: { listable: false, visible: true, disabled: false },
  filename: { listable: true, visible: true, disabled: false },
  description: { listable: false, visible: true, disabled: false },
  created: { listable: true, visible: true, disabled: true },
  released: { listable: true, visible: true, disabled: false },
  archived: { listable: true, visible: true, disabled: false },
  videoCategory: { listable: true, visible: true, disabled: false },
  coach: { listable: false, visible: true, disabled: false }
}
