import { IPlayList } from 'app/entities/play-list/play-list.model';

export interface IVideoItem {
  id: number;
  previous?: string | null;
  code?: string | null;
  next?: string | null;
  playList?: IPlayList | null;
}

export type NewVideoItem = Omit<IVideoItem, 'id'> & { id: null };
