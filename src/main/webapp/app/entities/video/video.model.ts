import dayjs from 'dayjs/esm';
import { IVideoCategory } from 'app/entities/video-category/video-category.model';
import { ICoach } from 'app/entities/coach/coach.model';
import { Language } from 'app/entities/enumerations/language.model';
import { Player } from 'app/entities/enumerations/player.model';
import { Intensity } from 'app/entities/enumerations/intensity.model';

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
