import dayjs from 'dayjs/esm';
import { IApsDevice } from 'app/entities/aps-device/aps-device.model';
import { IPlayList } from 'app/entities/play-list/play-list.model';
import { Language } from 'app/entities/enumerations/language.model';

export interface IApsUser {
  id: number;
  name?: string | null;
  username?: string | null;
  usernameVerified?: boolean | null;
  privateEmail?: string | null;
  privateVerified?: boolean | null;
  language?: keyof typeof Language | null;
  license?: string | null;
  active?: boolean | null;
  password?: string | null;
  created?: dayjs.Dayjs | null;
  updated?: dayjs.Dayjs | null;
  apsDevices?: IApsDevice[] | null;
  playLists?: IPlayList[] | null;
}

export type NewApsUser = Omit<IApsUser, 'id'> & { id: null };
