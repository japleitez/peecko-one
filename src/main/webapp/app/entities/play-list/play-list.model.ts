import dayjs from 'dayjs/esm';
import { IVideoItem } from 'app/entities/video-item/video-item.model';
import { IApsUser } from 'app/entities/aps-user/aps-user.model';

export interface IPlayList {
  id: number;
  name?: string | null;
  counter?: number | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  videoItems?: IVideoItem[] | null;
  apsUser?: IApsUser | null;
}

export type NewPlayList = Omit<IPlayList, 'id'> & { id: null };
