'use client';

import { Badge } from '@/components/ui/badge';
import { cn } from '@/lib/utils';

type SessionStatus = 'PENDING' | 'COMPLETED' | 'SKIPPED';

interface SessionStatusBadgeProps {
  status: SessionStatus;
}

const statusConfig: Record<SessionStatus, { label: string; className: string }> = {
  PENDING: {
    label: 'Pending',
    className: 'bg-muted text-muted-foreground hover:bg-muted',
  },
  COMPLETED: {
    label: 'Completed',
    className: 'bg-green-500/20 text-green-600 dark:text-green-400 hover:bg-green-500/30',
  },
  SKIPPED: {
    label: 'Skipped',
    className: 'bg-red-500/20 text-red-600 dark:text-red-400 hover:bg-red-500/30',
  },
};

export default function SessionStatusBadge({ status }: SessionStatusBadgeProps) {
  const config = statusConfig[status];
  
  return (
    <Badge variant="secondary" className={cn('font-medium', config.className)}>
      {config.label}
    </Badge>
  );
}
