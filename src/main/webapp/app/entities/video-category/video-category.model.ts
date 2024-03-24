import dayjs from 'dayjs/esm';
import { IVideo } from 'app/entities/video/video.model';
import { FieldAccess } from '../../shared/profile/view.models';

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

export interface VideoCategoryAccess {
  id: FieldAccess;
  code: FieldAccess;
  title: FieldAccess;
  label: FieldAccess;
  created: FieldAccess;
  released: FieldAccess;
  archived: FieldAccess;
  videos: FieldAccess;
}

export let VIDEO_CATEGORY_ACCESS: VideoCategoryAccess;

VIDEO_CATEGORY_ACCESS = {
  id: { listable: false, visible: true, disabled: true },
  code: { listable: true, visible: true, disabled: false },
  title: { listable: true, visible: true, disabled: false },
  label: { listable: true, visible: true, disabled: false },
  created: { listable: true, visible: true, disabled: true },
  released: { listable: true, visible: true, disabled: false },
  archived: { listable: true, visible: true, disabled: false },
  videos: { listable: true, visible: true, disabled: false }
}
