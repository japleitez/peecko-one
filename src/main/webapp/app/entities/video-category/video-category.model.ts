import dayjs from 'dayjs/esm';
import { IVideo } from 'app/entities/video/video.model';

export interface IVideoCategory {
  id: number;
  code?: string | null;
  title?: string | null;
  label?: string | null;
  created?: dayjs.Dayjs | null;
  released?: dayjs.Dayjs | null;
  archived?: dayjs.Dayjs | null;
  videos?: IVideo[] | null;
}

export type NewVideoCategory = Omit<IVideoCategory, 'id'> & { id: null };
