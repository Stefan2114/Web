export interface Task {
  id: number;
  title: string;
  status: string;
  user: User;
  lastUpdated: Date;
}

export interface User {
  id: number;
  username: string;
}

export interface TaskLog{
    id: number;
    task: Task;
    oldStatus: string;
    newStatus: string;
    timestamp: Date;
}

export interface UpdatedTask{
      id: number;
    newStatus: string;
    username: string;
}


