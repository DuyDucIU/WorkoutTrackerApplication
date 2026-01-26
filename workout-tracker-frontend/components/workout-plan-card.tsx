'use client';

import { WorkoutPlan } from '@/lib/api';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Calendar, Edit, Trash2, Eye } from 'lucide-react';
import Link from 'next/link';

interface WorkoutPlanCardProps {
  plan: WorkoutPlan;
  onEdit: (plan: WorkoutPlan) => void;
  onDelete: (plan: WorkoutPlan) => void;
}

export default function WorkoutPlanCard({ plan, onEdit, onDelete }: WorkoutPlanCardProps) {
  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      month: 'short',
      day: 'numeric',
      year: 'numeric',
    });
  };

  return (
    <Card className="bg-card border-border hover:border-primary/50 transition-colors group">
      <CardHeader className="pb-3">
        <CardTitle className="text-lg text-card-foreground group-hover:text-primary transition-colors">
          {plan.name}
        </CardTitle>
        {plan.description && (
          <CardDescription className="line-clamp-2">
            {plan.description}
          </CardDescription>
        )}
      </CardHeader>
      <CardContent className="pb-3">
        <div className="flex items-center gap-2 text-xs text-muted-foreground">
          <Calendar className="h-3 w-3" />
          <span>Created {formatDate(plan.createdAt)}</span>
        </div>
      </CardContent>
      <CardFooter className="flex gap-2 pt-0">
        <Link href={`/plans/${plan.id}/sessions`} className="flex-1">
          <Button variant="default" size="sm" className="w-full gap-2">
            <Eye className="h-4 w-4" />
            Sessions
          </Button>
        </Link>
        <Button
          variant="outline"
          size="sm"
          onClick={() => onEdit(plan)}
          className="gap-2"
        >
          <Edit className="h-4 w-4" />
          <span className="sr-only">Edit</span>
        </Button>
        <Button
          variant="outline"
          size="sm"
          onClick={() => onDelete(plan)}
          className="gap-2 text-destructive hover:text-destructive hover:bg-destructive/10"
        >
          <Trash2 className="h-4 w-4" />
          <span className="sr-only">Delete</span>
        </Button>
      </CardFooter>
    </Card>
  );
}
